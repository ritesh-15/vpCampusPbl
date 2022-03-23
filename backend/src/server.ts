import express, { Application, NextFunction, Request, Response } from "express";
import authRouter from "./routes/authRoutes";
import { errorMiddleware } from "./middlewares/errorMiddleware";
import { connection } from "./db/connection";
import cors from "cors";
import CreateHttpError from "./utils/errorHandler";
import morgan from "morgan";
import helmet from "helmet";
import cookieParser from "cookie-parser";
import { apiLimiter, authenticationLimiter } from "./constants/rateLimit";
import passport from "passport";
import { passportJwt } from "./middlewares/passportJwt";
import { userRouter } from "./routes/userRoutes";
import { notificationRouter } from "./routes/notificationRoutes";
import { Server } from "socket.io";

const app: Application = express();

const PORT = process.env.PORT || 9000;

// database connection
connection();

// middlewares

app.use("/api", apiLimiter);

app.use(cookieParser());

app.use(morgan("dev"));

app.use(helmet());

app.use(express.json({ limit: "100mb" }));

app.use(express.urlencoded({ extended: true, limit: "50mb" }));

// passport middleware

passport.initialize();

passportJwt(passport);

// routes
app.use("/api/v1/auth", authenticationLimiter, authRouter);

app.use("/api/v1/user", userRouter);

app.use("/api/v1/notification", notificationRouter);

// error handler

app.use((req: Request, res: Response, next: NextFunction) => {
  return next(CreateHttpError.notFound("No route match!"));
});

app.use(errorMiddleware);

const server = app.listen(PORT, () =>
  console.log(`Server is running on port ${PORT}`)
);

const io = new Server(server, {
  cors: {
    origin: "http://localhost:3000",
    methods: ["GET", "POST", "DELETE", "PUT"],
    credentials: true,
  },
});

io.on("connection", (socket) => {
  console.log(socket.id);
  console.log("User connected!");

  socket.on("join-notification-room", () => {
    socket.join("notification-room");

    socket.on("new-notification", (notification) => {
      console.log(notification);
      socket.in("notification-room").emit("new-notification", notification);
      io.to("notification-room").emit("new-notification", notification);
    });

    console.log("Notification room joined");
  });
});
