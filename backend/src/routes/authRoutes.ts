import { Router } from "express";
import multer from "multer";
import authController from "../controller/authController";
import { isAuthenticated } from "../middlewares/authMiddleware";

const router = Router();

router.route("/login").post(authController.login)

router.route("/register").post(authController.register);

router.route("/activate").put([isAuthenticated], authController.activate);

router.route("/logout").delete(isAuthenticated, authController.logout);

router.route("/refresh").get(authController.refresh);

router.route("/send-otp").post([isAuthenticated],authController.sendOtp);

router.route("/verify-otp").post([isAuthenticated],authController.verify);

export default router;
