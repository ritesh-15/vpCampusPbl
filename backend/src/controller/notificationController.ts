import { NextFunction, Request, Response } from "express";
import { UserInterface } from "../interfaces/UserInterface";
import Notification from "../model/notificationModel";
import CreateHttpError from "../utils/errorHandler";

class NotificationController {
  // @route POST/create
  // @desc Create a new notification
  // @access private
  async createNewNotification(req: Request, res: Response, next: NextFunction) {
    const user = <UserInterface>req.user;

    const { title, description } = req.body;

    if (!title || !description)
      return next(
        CreateHttpError.forbidden("Title and description is required!")
      );

    try {
      const notification = await Notification.create({
        title,
        description,
        userId: user._id,
      });

      await notification.populate("userId");

      return res.json({
        ok: true,
        notification,
      });
    } catch (error) {
      next(CreateHttpError.internalServerError("Internal server error!"));
    }
  }

  // @route PUT /:id
  // @desc Update notification
  // @access private
  async updateNotification(req: Request, res: Response, next: NextFunction) {
    const user = <UserInterface>req.user;

    const { id } = req.params;

    const { title, description } = req.body;

    if (!id)
      return next(CreateHttpError.forbidden("Notification id is not found!"));

    if (!title || !description)
      return next(
        CreateHttpError.forbidden("Title and description is required!")
      );

    try {
      let notification = await Notification.findOne({
        $and: [
          {
            _id: id,
          },
          {
            userId: user._id,
          },
        ],
      });

      if (!notification)
        return next(
          CreateHttpError.notFound("Notification with given id is not found!")
        );

      notification = await Notification.findOneAndUpdate(
        { _id: id },
        {
          $set: {
            title,
            description,
          },
        },
        { new: true }
      ).populate("userId");

      return res.json({
        ok: true,
        notification,
      });
    } catch (error) {
      next(CreateHttpError.internalServerError("Internal server error!"));
    }
  }

  // @route DELETE /:id
  // @desc DELETE notification
  // @access private
  async deleteNotification(req: Request, res: Response, next: NextFunction) {
    const user = <UserInterface>req.user;

    const { id } = req.params;

    if (!id)
      return next(CreateHttpError.forbidden("Notification id is not found!"));

    try {
      const notification = await Notification.findOne({
        $and: [
          {
            _id: id,
          },
          {
            userId: user._id,
          },
        ],
      });

      if (!notification)
        return next(
          CreateHttpError.notFound("Notification with given id is not found!")
        );

      await notification.remove();

      return res.json({
        ok: true,
        message: "Notification deleted successfully!",
      });
    } catch (error) {
      next(CreateHttpError.internalServerError("Internal server error!"));
    }
  }

  // @route GET /:id
  // @desc Get single  notification
  // @access public
  async getSingleNotification(req: Request, res: Response, next: NextFunction) {
    const { id } = req.params;

    if (!id)
      return next(CreateHttpError.forbidden("Notification id is not found!"));

    try {
      const notification = await Notification.findOne({ _id: id });

      if (!notification)
        return next(
          CreateHttpError.notFound("Notification with given id is not found!")
        );

      await notification.populate("userId");

      return res.json({
        ok: true,
        notification,
      });
    } catch (error) {
      next(CreateHttpError.internalServerError("Internal server error!"));
    }
  }

  // @route GET /
  // @desc Get all  notification
  // @access public
  async getAllNotifications(req: Request, res: Response, next: NextFunction) {
    // TODO implement public and private access
    try {
      const notifications = await Notification.find().populate("userId");

      return res.json({
        ok: true,
        notifications,
      });
    } catch (error: any) {
      next(CreateHttpError.internalServerError("Internal server error!"));
    }
  }
}

export default new NotificationController();
