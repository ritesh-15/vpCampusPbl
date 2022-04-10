import { NextFunction, Request, Response } from "express";
import { ClubInterface } from "../interfaces/ClubInterface";
import { UserInterface } from "../interfaces/UserInterface";
import Clubs from "../model/clubsModal";
import CreateHttpError from "../utils/errorHandler";

class ClubController {
  // @route POST club/create
  // #des create new club
  // @access private
  static async createNewClub(req: Request, res: Response, next: NextFunction) {
    const { name, description, avatar } = req.body;
    const currentUser = req.user as UserInterface;

    if (!name || !description || !avatar.url || !avatar.publicId)
      return next(CreateHttpError.badRequest("All fields are required!"));

    try {
      const isExits = await Clubs.findOne({ name });

      if (isExits)
        return next(
          CreateHttpError.forbidden(
            "The name of the club is already taken by some another club plese use another name!"
          )
        );

      const club = new Clubs({
        name,
        description,
        admin: currentUser._id,
        avatar,
      });

      await club.save();

      await club.populate("admin");

      return res.status(201).json({
        ok: true,
        club: club,
      });
    } catch (error: any) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route PUT club/:id
  // #des update club information
  // @access private
  static async updateClubInfo(req: Request, res: Response, next: NextFunction) {
    const { name, description, avatar } = req.body;
    const currentUser = req.user as UserInterface;
    const { id } = req.params;

    if (!id) return next(CreateHttpError.badRequest("Club id not found!"));

    try {
      const isExits = await Clubs.findOne({ _id: id });

      if (!isExits)
        return next(CreateHttpError.forbidden("Club with this id not found!"));

      if (currentUser._id.toString() !== isExits?.admin.toString()) {
        return next(
          CreateHttpError.forbidden(
            "Only admin has access to edit the club information!"
          )
        );
      }

      const club = await Clubs.findOneAndUpdate(
        {
          _id: id,
        },
        {
          $set: {
            name: name,
            description: description,
            avatar: avatar,
          },
        },
        { new: true }
      ).populate("admin");

      return res.status(201).json({
        ok: true,
        club: club,
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route DELETE club/:id
  // #des delete club
  // @access private
  static async deleteClub(req: Request, res: Response, next: NextFunction) {
    const currentUser = req.user as UserInterface;
    const { id } = req.params;

    if (!id) return next(CreateHttpError.badRequest("Club id not found!"));

    try {
      const isExits = await Clubs.findOne({ _id: id });

      if (!isExits)
        return next(CreateHttpError.forbidden("Club with this id not found!"));

      if (currentUser._id.toString() !== isExits?.admin.toString()) {
        return next(
          CreateHttpError.forbidden(
            "Only admin has access to edit the club information!"
          )
        );
      }

      await isExits.remove();

      return res.status(201).json({
        ok: true,
        message: "Club deleted successfully!",
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route GET club/:id
  // #des get single club information
  // @access private
  static async getClubInformation(
    req: Request,
    res: Response,
    next: NextFunction
  ) {
    const { id } = req.params;

    if (!id) return next(CreateHttpError.badRequest("Club id not found!"));

    try {
      const club = await Clubs.findOne({ _id: id }).populate("admin");

      if (!club)
        return next(CreateHttpError.forbidden("Club with this id not found!"));

      return res.status(201).json({
        ok: true,
        club: club,
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }

  // @route GET club/clubs
  // #des create new club
  // @access private
  static async getAllClubs(req: Request, res: Response, next: NextFunction) {
    try {
      const clubs = await Clubs.find().populate("admin");

      return res.json({
        ok: true,
        clubs: clubs,
      });
    } catch (error) {
      return next(
        CreateHttpError.internalServerError("Internal server error!")
      );
    }
  }
}

export default ClubController;
