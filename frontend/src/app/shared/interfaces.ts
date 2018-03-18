export interface LoginCredentials {
  email: string;
  password: string;
}

export interface JWTToken {
  jwtToken: string;
}

export interface IResponseBody {
  status?: number;
  title?: string;
  detail?: string;
  message?: string;
}
