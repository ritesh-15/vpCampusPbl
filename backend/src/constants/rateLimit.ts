import rateLimit from "express-rate-limit";
import CreateHttpError from "../utils/errorHandler";

export const apiLimiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 minutes
  max: 100,
  standardHeaders: true,
  legacyHeaders: false,
  handler: (req, res, next, option) => {
    next(CreateHttpError.toManyRequest(option.message));
  },
});

export const authenticationLimiter = rateLimit({
  windowMs: 60 * 60 * 1000, // 1 hour
  max: 100, // change it to 5 in production mode
  message:
    "Too many accounts created from this IP, please try again after an hour",
  standardHeaders: true,
  legacyHeaders: false,
  handler: (req, res, next, option) => {
    next(CreateHttpError.toManyRequest(option.message));
  },
});
