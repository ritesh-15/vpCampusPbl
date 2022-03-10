import { Schema, model } from "mongoose";
import { ACCESS_ROLE } from "../constants/NotificationConstats";
import { NotificationInterface } from "../interfaces/NotificationInterface";

const notificationSchema = new Schema<NotificationInterface>(
  {
    userId: {
      type: Schema.Types.ObjectId,
      required: true,
      ref: "users",
    },
    title: {
      type: String,
      required: true,
    },
    description: {
      type: String,
      required: true,
    },
    attachments: [
      {
        publicId: {
          type: String,
          default: "",
        },
        url: {
          type: String,
          default: "",
        },
        type: {
          type: String,
          default: "",
        },
      },
    ],
    access: {
      type: String,
      default: ACCESS_ROLE.PUBLIC,
    },
  },
  { timestamps: true }
);

const Notification = model<NotificationInterface>(
  "notifications",
  notificationSchema
);

export default Notification;
