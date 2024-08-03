import { Button } from "antd";
import { useNavigate } from "react-router-dom";

interface ChangeInventoryAmountButtonProps {
  searchEl: string;
  type: string;
}

const ChangeInventoryAmountButton = ({searchEl, type}: ChangeInventoryAmountButtonProps) => {

  const navigate = useNavigate();
  const onClick = () => {
    navigate("/" + type + "/" + searchEl + "/changeAmount", { state: { value: searchEl } });
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
      Change amount
    </Button>
  );
};

export default ChangeInventoryAmountButton;