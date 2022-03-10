import { NextFunction, Request, Response } from "express";
import { ADMIN } from "../constants/userRoles";
import { UserInterface } from "../interfaces/UserInterface";
import CreateHttpError from "../utils/errorHandler";

export const isAdmin = (req: Request, res: Response, next: NextFunction) => {
  const user = <UserInterface>req.user;

  if (user.role != ADMIN) {
    return next(
      CreateHttpError.badRequest(
        "You need to be an admin to perform this action!"
      )
    );
  }

  next();
};
