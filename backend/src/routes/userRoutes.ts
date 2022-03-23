import { Router } from "express";
import userController from "../controller/userController";
import { isAuthenticated } from "../middlewares/authMiddleware";

const router = Router();

router
  .route("/update-profile")
  .put(isAuthenticated, userController.updateProfile);

router.route("/get").get(isAuthenticated, userController.getUserById);

export { router as userRouter };
