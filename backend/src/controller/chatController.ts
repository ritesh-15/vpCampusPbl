import { NextFunction, Request, Response } from "express";
import { UserInterface } from "../interfaces/UserInterface";
import Chat from "../model/chatModal";
import Clubs from "../model/clubsModal";
import CreateHttpError from "../utils/errorHandler";

class ChatController {
  // @route POST/new chat/:id
  // @desc new hcat
  // @access private
  static async newChat(req: Request, res: Response, next: NextFunction) {
    const { message } = req.body;
    const { id } = req.params;
    const currentUser = req.user as UserInterface;

    if (!id) return next(CreateHttpError.badRequest("Club id not found!"));

    if (!message) return next(CreateHttpError.badRequest("Message not found!"));

    try {
      const club = await Clubs.findOne({ _id: id }).populate("admin");

      if (!club)
        return next(
          CreateHttpError.notFound("Club with given id is not found!")
        );

      const chat = await Chat.create({
        clubId: club._id,
        userId: currentUser._id,
        message,
      });

      await (await chat.populate("userId")).populate("clubId");

      return res.status(201).json({
        ok: true,
        chat: chat,
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route GET/chats/:id
  // @desc get club chats
  // @access private
  static async getClubChats(req: Request, res: Response, next: NextFunction) {
    const { id } = req.params;

    if (!id) return next(CreateHttpError.badRequest("Club id not found!"));

    try {
      const chats = await Chat.find({ clubId: id })
        .populate("userId")
        .populate("clubId");

      return res.json({
        ok: true,
        chats: chats,
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }
}

export default ChatController;
