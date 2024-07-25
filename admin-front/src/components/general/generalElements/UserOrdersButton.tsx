import { Button } from "antd";
import { useNavigate } from "react-router-dom";

interface UserOrdersButtonProps {
  username: string;
}


const UserOrdersButton = ({username}: UserOrdersButtonProps) => {

  const navigate = useNavigate();
  const onClick = () => {
    navigate("/users/" + username + "/orders/show", { state: { value: username } });
  };

  return (
    <Button
      type="primary"
      onClick={onClick}
      ghost
      style={{
        color: "green",
        borderColor: "green",
        width: "50%",
        height: "5vh",
        fontWeight: 700,
      }}
    >
      Orders
    </Button>
  );
};

export default UserOrdersButton;