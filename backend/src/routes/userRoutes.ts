import { Router } from "express";
import userController from "../controller/userController";
import { isAuthenticated } from "../middlewares/authMiddleware";

const router = Router();

router
  .route("/update-profile")
  .put(isAuthenticated, userController.updateProfile);

export { router as userRouter };
