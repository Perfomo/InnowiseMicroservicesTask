import { Button } from "antd";
import axios from "axios";
import CartManager from "../../CartManager";

interface CostTagProps {
  cost: string | null;
}

const onClick = () => {
  console.log(CartManager.parseCartToOrderRequest());
  if (!localStorage.getItem("token")) {
    window.location.href = "/login";
  }
  else {
    console.log(CartManager.parseCartToOrderRequest());
    axios
    .post("http://172.17.0.1:8081/orders/api/orders", CartManager.parseCartToOrderRequest(), {
      headers: {
        "Authorization": "Bearer " + localStorage.getItem("token"),
        "Content-Type": "application/json"
      },
    })
    .then ((response) => {
      localStorage.removeItem("cart")
      localStorage.removeItem("cartCost")
      window.location.href = "/orders";
    })
    .catch((error) => {
      console.log(error);
    });
  }
};

const MakeOrderButton = ({ cost }: CostTagProps) => {
  return (
    <Button
      onClick={onClick}
      type="primary"
      ghost
      style={{ height: "4vh", fontWeight: 700 }}
    >
      {cost}$
    </Button>
  );
};

export default MakeOrderButton;
