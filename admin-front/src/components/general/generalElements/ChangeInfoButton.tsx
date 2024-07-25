import { Button } from "antd";
import { useNavigate } from "react-router-dom";

interface ChangeInfoButtonProps {
  searchEl: string;
  type: string;
}

const ChangeInfoButton = ({searchEl, type}: ChangeInfoButtonProps) => {

  const navigate = useNavigate();
  const onClick = () => {
    navigate("/" + type + "/" + searchEl + "/changeInfo", { state: { value: searchEl } });
  };
  
  return (
    <Button
      onClick={onClick}
      type="primary"
      ghost
      style={{
        color: "green",
        borderColor: "green",
        width: "50%",
        height: "5vh",
        marginTop: "2%",
        fontWeight: 700,
      }}
    >
      Change info
    </Button>
  );
};

export default ChangeInfoButton;