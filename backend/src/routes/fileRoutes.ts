import { Router } from "express";
import fileUploadController from "../controller/fileUploadController";
import { isAuthenticated } from "../middlewares/authMiddleware";

const router = Router()

router.route("/upload-single")
    .post([isAuthenticated],fileUploadController.uploadSingleFile)


router.route("/upload-multiple")
    .post([isAuthenticated],fileUploadController.uploadMultipleFiles)

export {router as uploadRouter}