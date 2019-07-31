import React from 'react';
import { FormikErrors } from 'formik';

interface FeedbackProps {
    message: string | FormikErrors<any>;
    isInvalid: boolean;
}

export default function Feedback(props: FeedbackProps) {
    const {message, isInvalid} = props;
    return <div className="invalid-feedback" style={{display: isInvalid ? 'block' : 'none'}}>{message}</div>;
}
