import { NextFunction, Request, Response } from "express";
import { UserInterface } from "../interfaces/UserInterface";
import CreateHttpError from "../utils/errorHandler";

export const isActivated = (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  const user = <UserInterface>req.user;

  if (!user.isActivated) {
    return next(
      CreateHttpError.badRequest("You need to activate your account first!")
    );
  }

  next();
};
