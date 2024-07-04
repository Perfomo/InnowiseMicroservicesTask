import React, { ReactNode } from "react";
import Alert from "./Alert";
interface AlertButtonProps {
    onClickButton: () => void;
}

const AlertButton = ({ onClickButton }: AlertButtonProps) => {
  return <button type="button" className="btn btn-primary" onClick={ () => {onClickButton()}}>Primary</button>;
};

export default AlertButton;
