export interface EndpointResponse<T> {
    success: boolean;
    data: T;
    errorMessage: string;
}
