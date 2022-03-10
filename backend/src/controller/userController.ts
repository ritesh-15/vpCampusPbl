import { NextFunction, Request, Response } from "express";
import { ADMIN, FACULTY, USER } from "../constants/userRoles";
import { UserInterface } from "../interfaces/UserInterface";
import User from "../model/userModel";
import CreateHttpError from "../utils/errorHandler";
import UploadService from "../utils/uploadFileService";

class UserController {
  // @route PUT/update-profile
  // @desc Update user profile
  // @access private

  async updateProfile(req: Request, res: Response, next: NextFunction) {
    const { avatar, department, yearOfStudy, name, bio } = req.body;

    const user = <UserInterface>req.user;

    try {
      let avatarInfo = null;

      if (avatar) {
        if (user.avatar.publicId)
          await UploadService.deletePreviousAvatar(user.avatar.publicId);

        avatarInfo = await new UploadService(avatar).uploadAsAvatar();
      }

      const updatedUser = await User.findOneAndUpdate(
        { _id: user._id },
        {
          $set: {
            name,
            department,
            yearOfStudy,
            bio,
            avatar: avatarInfo
              ? {
                  publicId: avatarInfo.public_id,
                  url: avatarInfo.url,
                }
              : user.avatar,
          },
        },
        { new: true }
      );

      return res.json({
        ok: true,
        user: updatedUser,
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route GET/all-users
  // @desc Get all users
  // @access admin
  async allUsers(req: Request, res: Response, next: NextFunction) {
    const { sort } = req.query;

    try {
      const users = sort
        ? await User.find().sort({ createdAt: -1 })
        : await User.find();

      return res.json({
        ok: true,
        users,
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route PUT/delete-user
  // @desc delete user
  // @access admin
  async deleteUser(req: Request, res: Response, next: NextFunction) {
    const { userId } = req.query;

    if (!userId)
      return next(CreateHttpError.badRequest("User id is required!"));

    try {
      const user = await User.findOne({ _id: userId });

      if (!user) return next(CreateHttpError.notFound("User not found!"));

      await User.deleteOne({ _id: userId });

      return res.json({
        ok: true,
        message: "User deleted succesfully!",
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route PUT/update-user/role
  // @desc update user role
  // @access admin
  async updateRole(req: Request, res: Response, next: NextFunction) {
    const { userId, role } = req.query;

    if (!userId || !role)
      return next(CreateHttpError.badRequest("User id and role is required!"));

    if (role != USER && role != ADMIN && role != FACULTY)
      return next(CreateHttpError.badRequest("Role is invalid!"));

    try {
      const user = await User.findOne({ _id: userId });

      if (!user) return next(CreateHttpError.notFound("User not found!"));

      user.role = role;

      await user.save();

      return res.json({
        ok: true,
        user,
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }
}

export default new UserController();
