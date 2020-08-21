export class FetchError extends Error {
  data: unknown;

  code: number;

  constructor(message: string, code: number, data?: unknown) {
    super();
    this.message = message;
    this.data = data;
    this.code = code;
  }
}
