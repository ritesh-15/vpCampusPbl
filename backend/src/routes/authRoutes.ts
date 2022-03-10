import { Router } from "express";
import authController from "../controller/authController";
import { isAuthenticated } from "../middlewares/authMiddleware";

const router = Router();

router.route("/login").post(authController.login);

router.route("/register").post(authController.register);

router.route("/activate").put(isAuthenticated, authController.activate);

router.route("/logout").delete(isAuthenticated, authController.logout);

router.route("/refresh").get(authController.refresh);

export default router;
