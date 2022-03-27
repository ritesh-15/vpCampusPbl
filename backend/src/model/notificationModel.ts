import { Schema, model } from "mongoose";
import { ACCESS_ROLE } from "../constants/NotificationConstats";
import { NotificationInterface } from "../interfaces/NotificationInterface";
import createDomPurify from "dompurify";
import { JSDOM } from "jsdom";
import { marked } from "marked";

const window: any = new JSDOM().window;

const purify = createDomPurify(window);

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
    html: {
      type: String,
      required: true,
    },
  },
  { timestamps: true }
);

notificationSchema.pre("validate", function (next) {
  if (this.description) {
    this.html = purify.sanitize(marked.parse(this.description));
  }

  next();
});

const Notification = model<NotificationInterface>(
  "notifications",
  notificationSchema
);

export default Notification;
