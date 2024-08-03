import { ShoppingCartOutlined } from "@ant-design/icons";
import { Button } from "antd";

const OrderCartButton = () => {
  return (
    <Button
      href="/cart"
      type="primary"
      ghost
      style={{
        color: "green",
        borderColor: "green",
        width: "10%",
        height: "6vh",
        fontWeight: 700,
      }}
    >
      <ShoppingCartOutlined />
    </Button>
  );
};

export default OrderCartButton;
