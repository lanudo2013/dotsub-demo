import React, { Component, ReactNode } from "react";
import { Alert } from "react-bootstrap";

interface AlertProps {
    isError: boolean;
    message: string;
    onClose: () => void;
}
interface AlertState {
    show: boolean;
}

const MESSAGE_FADE_OUT_TIME = 6;

export default class AlertMessage extends Component<AlertProps, AlertState> {
    private to: NodeJS.Timeout;

    constructor(props: AlertProps) {
        super(props);
        this.state = {
            show: true,
        };
    }

    componentDidMount() {
        this.to = setTimeout(() => {
            this.props.onClose();
        }, MESSAGE_FADE_OUT_TIME * 1000);
    }

    componentWillUnmount() {
        clearTimeout(this.to);
    }

    public render(): ReactNode {
        return <Alert variant={this.props.isError ? 'danger' : 'success'} >
            {this.props.message}
        </Alert>;
    }
}
