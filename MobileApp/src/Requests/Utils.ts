export class LoginError {
  message: string;

  constructor(message: string) {
    this.message = message;
  }
}

export class RegisterError {
  message: string;

  constructor(message: string) {
    this.message = message;
  }
}

export function headers(token: string) {
  return {Authorization: `Bearer ${token}`};
}
