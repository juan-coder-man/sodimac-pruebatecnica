import { ApiError } from './api-response.model';

export interface UiFailure {
  code: string;
  message: string;
  fieldErrors: ApiError[];
  httpStatus?: number;
}
