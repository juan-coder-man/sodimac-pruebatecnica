export interface ApiError {
  field: string | null;
  code: string;
  message: string;
}

export interface ApiResponse<T> {
  success: boolean;
  code: string;
  message: string;
  data: T | null;
  errors: ApiError[];
}
