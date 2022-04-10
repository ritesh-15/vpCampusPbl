import { Router } from "express";
import ChatController from "../controller/chatController";
import { isAuthenticated } from "../middlewares/authMiddleware";

const router = Router();

router.route("/new-chat/:id").post(isAuthenticated, ChatController.newChat);
router.route("/chats/:id").get(isAuthenticated, ChatController.getClubChats);

export default router;
