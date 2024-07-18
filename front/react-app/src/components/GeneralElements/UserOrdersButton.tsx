import { Button } from "antd";

const UserOrdersButton = () => {
  return (
    <Button
      href="/userOrders"
      type="primary"
      ghost
      style={{
        color: "green",
        borderColor: "green",
        width: "50%",
        height: "6vh",
        fontWeight: 700,
      }}
    >
      Orders
    </Button>
  );
};

export default UserOrdersButton;