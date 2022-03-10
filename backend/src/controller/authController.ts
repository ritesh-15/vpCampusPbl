import { NextFunction, Request, Response } from "express";
import User from "../model/userModel";
import CreateHttpError from "../utils/errorHandler";
import bcrypt from "bcrypt";
import { setResponseToken } from "../utils/setResponseToken";
import AuthValidation from "../validations/authValidations";
import UploadService from "../utils/uploadFileService";
import Token from "../model/tokenModel";
import { UserInterface } from "../interfaces/UserInterface";
import JWTToken from "../utils/tokenService";

class AuthController {
  // @route   POST /login
  // @desc    Login user
  // @access  Public
  async login(req: Request, res: Response, next: NextFunction) {
    const { email, password } = req.body;

    if(!email || !password)
    return next(CreateHttpError.forbidden("Email address and password is required!"))

    try {
      // check if user already exists with this email
      const user = await User.findOne({ email }).select("+password");

      if (!user)
        return next(
          CreateHttpError.forbidden("User with this email does not exists!")
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
        },
        tokens
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
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
      // upload the avatar
      const avatarInfo = await new UploadService(avatar).uploadAsAvatar();

      // update user
      const updatedUser = await User.findOneAndUpdate(
        { _id: user._id },
        {
          $set: {
            yearOfStudy,
            bio,
            department,
            isActivated: true,
            avatar: {
              publicId: avatarInfo.public_id,
              url: avatarInfo.url,
            },
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
      console.log(error);
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route GET/refresh
  // @desc Refresh token
  // @access public

  async refresh(req: Request, res: Response, next: NextFunction) {
    let { refreshToken: recivedRefreshToken } = req.cookies;

    if(!recivedRefreshToken){
      const token = req.headers['refreshtoken']
      recivedRefreshToken = token?.toString().split(" ")[1]
    }

    if (!recivedRefreshToken)
      return next(CreateHttpError.notFound("Refresh token is required!"));

    try {
      const tokenInfo = JWTToken.verifyRefreshToken(recivedRefreshToken);

      const user = await User.findById(tokenInfo._id);

      if (!user) throw new Error("User not exists!");

      // revoke last refresh tokens
      await Token.deleteMany({ userId: user._id });

      const tokens = await setResponseToken(res, user);

      return res.json({ ok: true, user ,tokens});
    } catch (error) {
      return next(CreateHttpError.unauthorized("Unauthorised!"));
    }
  }
}

export default new AuthController();
