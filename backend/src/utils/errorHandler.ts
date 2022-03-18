class CreateHttpError {
  message: string;
  status: number;

  constructor(message: string, status: number) {
    this.message = message;
    this.status = status;
  }

  static badRequest(message: string) {
    return new CreateHttpError(message, 400);
  }

  static unauthorized(message: string) {
    return new CreateHttpError(message, 401);
  }

  static forbidden(message: string) {
    return new CreateHttpError(message, 403);
  }

  static notFound(message: string) {
    return new CreateHttpError(message, 404);
  }

  static internalServerError(message: string) {
    return new CreateHttpError(message, 500);
  }

  static toManyRequest(message: string) {
    return new CreateHttpError(message, 429);
  }
}

export default CreateHttpError;
