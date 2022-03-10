import { Router } from "express";
import notificationController from "../controller/notificationController";
import { isAuthenticated } from "../middlewares/authMiddleware";

const router = Router();

router
  .route("/create")
  .post([isAuthenticated], notificationController.createNewNotification);

router
  .route("/all")
  .get([isAuthenticated],notificationController.getAllNotifications);

router
  .route("/:id")
  .put([isAuthenticated], notificationController.updateNotification)
  .delete([isAuthenticated], notificationController.deleteNotification)
  .get([isAuthenticated],notificationController.getSingleNotification);

export { router as notificationRouter };
