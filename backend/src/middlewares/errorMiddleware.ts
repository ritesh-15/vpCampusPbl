import { NextFunction, Request, Response } from "express";
import CreateHttpError from "../utils/errorHandler";

export const errorMiddleware = (
  error: Error,
  req: Request,
  res: Response,
  next: NextFunction
) => {
  if (error instanceof CreateHttpError) {
    return res.status(error.status).send({
      message: error.message,
      status: error.status,
      ok: false,
    });
  }



  return res.status(500).send({
    message: "Internal Server Error",
    status: 500,
    ok: false,
  });
};
