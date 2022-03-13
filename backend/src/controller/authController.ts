import { NextFunction, Request, Response } from "express";
import User from "../model/userModel";
import CreateHttpError from "../utils/errorHandler";
import bcrypt from "bcrypt";
import { setResponseToken } from "../utils/setResponseToken";
import UploadService from "../services/uploadFileService";
import Token from "../model/tokenModel";
import { UserInterface } from "../interfaces/UserInterface";
import JWTToken from "../services/tokenService";
import OtpService from "../services/OtpService";
import EmailService from "../services/emailService";

class AuthController {
  // @route   POST /login
  // @desc    Login user
  // @access  Public
  async login(req: Request, res: Response, next: NextFunction) {
    const { email, password } = req.body;

    if(!email || !password)
      return next(CreateHttpError.notFound("Email address and password is required!"))

    try {
      // check if user already exists with this email
      const user = await User.findOne({ email }).select("+password");


      if (!user)
        return next(
          CreateHttpError.notFound("User with this email does not exists!")
        );

      // compare user password
      const isAuthenticated = await bcrypt.compare(password, user.password);



      if (!isAuthenticated)
        return next(
          CreateHttpError.unauthorized(
            "Email address or password is incorrect!"
          )
        );

     const tokens =  await setResponseToken(res, user);

      return res.status(200).json({
        ok: true,
        user: {
          _id: user._id,
          email: user.email,
          name: user.name,
          isActivated: user.isActivated,
          avatar: user.avatar,
          department: user.department,
          yearOfStudy: user.yearOfStudy,
          bio: user.bio,
          role: user.role,
          isVerified:user.isVerified
        },
        tokens
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route POST/register
  // @desc Register user
  // @access public

  async register(req: Request, res: Response, next: NextFunction) {
    const { email, password, name } = req.body;

    if (!email || !password || !name)
      return next(
        CreateHttpError.badRequest(
          "Email address, name and password is required!"
        )
      );

    try {
      const isUserExists = await User.findOne({ email });

      if (isUserExists)
        return next(
          CreateHttpError.forbidden(
            "User is already exists with this email address!"
          )
        );

      const hashedPassword = await bcrypt.hash(password, 10);

      const body = {
        email,
        password: hashedPassword,
        name,
      };

    const user = await User.create(body);

    const newOtp = new OtpService(email)
      
    const hashedOtp = `${newOtp.hash()}.${newOtp.expiresIn}`

    const newEmail = new EmailService(
      {
        to:email,
        subject:"Account verification!",
        html:EmailService.generateOtpTemplate(user.name,newOtp.otp),
        text:"Account verification!"
      }
    )

    await newEmail.send()

     const tokens  =  await setResponseToken(res, user);

      return res.json({
        ok: true,
        user: {
          _id: user._id,
          email: user.email,
          name: user.name,
          isActivated: user.isActivated,
          avatar: user.avatar,
          department: user.department,
          yearOfStudy: user.yearOfStudy,
          bio: user.bio,
          role: user.role,
          isVerified:user.isVerified
        },
        tokens,
        otp: {
          hash: hashedOtp,
          email: email,
          otp: newOtp.otp,
        },
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route POST/send-otp
  // @desc send otp
  // @access public
  async sendOtp(req: Request, res: Response, next: NextFunction){
    const {email} = req.body
    const currentUser = req.user!! as UserInterface

    if(!email)
      return next(CreateHttpError.badRequest("Email address is not found!"))

    if(email != currentUser.email){
      return next(CreateHttpError
        .badRequest("Email address not match with account email address!"))
    }  

    try {
      const user = await User.findOne({email})

      if(!user)
        return next(CreateHttpError.notFound("User with this email is not found!"))

      const newOtp = new OtpService(email)
      
      const hashedOtp = `${newOtp.hash()}.${newOtp.expiresIn}`

      const newEmail = new EmailService(
        {
          to:email,
          subject:"Account verification!",
          html:EmailService.generateOtpTemplate(user.name,newOtp.otp),
          text:"Account verification!"
        }
      )

    await newEmail.send()

      return res.json({
        ok: true,
        otp: {
          hash: hashedOtp,
          email: email,
          otp: newOtp.otp,
        },
      });
    

    } catch (error) {
      return next(CreateHttpError.internalServerError("Internal server error!"))
    }  

  }

  // @route PUT/verify
  // @desc verify account
  // @access private
  async verify(req: Request, res: Response, next: NextFunction){
    const currentUser = <UserInterface>req.user

    const {otp,email,hash} = req.body

    if(!otp || !email || !hash)
      return next(CreateHttpError.badRequest("All fileds are required!"))


    try {
      if(currentUser.email != email)
        return next(CreateHttpError.badRequest("Account email address not match!"))

      const givenHash = <string>hash;

      const expiresIn = parseInt(givenHash.split(".")[1]);
  
      const hashedOtp = givenHash.split(".")[0];
  
      if (Date.now() > expiresIn)
        return next(CreateHttpError.badRequest("Otp expires!"));
  
      const isValideOtp = OtpService.verify(email, otp, expiresIn, hashedOtp);
  
      if (!isValideOtp)
        return next(CreateHttpError.unauthorized("Otp does not match!"));

      const user = await User.findOneAndUpdate({_id:currentUser._id},
        {
          $set:{
            isVerified:true
          }
        },{new:true})

      return res.json({
        ok:true,
        user
      })  

    } catch (error) {
      return next(CreateHttpError.internalServerError("Internal server error!"))
    }  

  }

  // @route PUT/activate
  // @desc Activate user
  // @access private
  async activate(req: Request, res: Response, next: NextFunction) {
    const { avatar, department, yearOfStudy, bio } = req.body;
    const user = <UserInterface>req.user;

    if (!avatar || !department || !yearOfStudy || !bio)
      return next(
        CreateHttpError.forbidden(
          "Avatar, department, year of study and bio is required!"
        )
      );

    try {
      // update user
      const updatedUser = await User.findOneAndUpdate(
        { _id: user._id },
        {
          $set: {
            yearOfStudy,
            bio,
            department,
            isActivated: true,
            avatar
          },
        },
        { new: true, runValidators: true }
      );

      return res.json({
        ok: true,
        user: updatedUser,
      });
    } catch (error: any) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route DELETE/logout
  // @desc Logout user
  // @access private
  async logout(req: Request, res: Response, next: NextFunction) {
    const user: UserInterface = <UserInterface>req.user;

    try {
      await Token.deleteMany({ userId: user._id });

      res.clearCookie("accessToken");

      res.clearCookie("refreshToken");

      return res.json({ ok: true, message: "Logged out successfull!" });
    } catch (error: any) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route GET/refresh
  // @desc Refresh token
  // @access public

  async refresh(req: Request, res: Response, next: NextFunction) {
    let recivedRefreshToken:string | undefined;

    if(req.cookies){
      recivedRefreshToken = req.cookies.refreshToken
    }

    if(!recivedRefreshToken){
      const token = req.headers['refreshtoken']
      recivedRefreshToken = token?.toString().split(" ")[1]
    }

    if (!recivedRefreshToken)
      return next(CreateHttpError.notFound("Refresh token is required!"));

    try {
      const tokenInfo = JWTToken.verifyRefreshToken(recivedRefreshToken);

      const isTokenExists = await Token.findOne({token:recivedRefreshToken})

      if(!isTokenExists)
        throw new Error("Session exipred!")

      const user = await User.findById(tokenInfo._id);

      if (!user) throw new Error("User not exists!");

      // revoke last refresh tokens
      await Token.deleteMany({ userId: user._id });

      // setting up new tokens
      const tokens = await setResponseToken(res, user);

      return res.json({ ok: true, user ,tokens});
    } catch (error) {
      return next(CreateHttpError.unauthorized("Unauthorised!"));
    }
  }
}

export default new AuthController();
