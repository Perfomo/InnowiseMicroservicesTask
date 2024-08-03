import { Button } from "antd";
import { useNavigate } from "react-router-dom";

interface ProductInInventoryButtonProps {
  name: string;
}


const ProductInInventoryButton = ({name}: ProductInInventoryButtonProps) => {

  const navigate = useNavigate();
  const onClick = () => {
    navigate("/inventory/" + name + "/show", { state: { value: name } });
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
      Inventory
    </Button>
  );
};

export default ProductInInventoryButton;