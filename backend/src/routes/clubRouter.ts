import { Router } from "express";
import ClubController from "../controller/clubController";
import { isAuthenticated } from "../middlewares/authMiddleware";

const router = Router();

router.route("/create").post(isAuthenticated, ClubController.createNewClub);

router.route("/clubs").get(isAuthenticated, ClubController.getAllClubs);

router
  .route("/:id")
  .put(isAuthenticated, ClubController.updateClubInfo)
  .delete(isAuthenticated, ClubController.deleteClub)
  .get(isAuthenticated, ClubController.getClubInformation);

export default router;
