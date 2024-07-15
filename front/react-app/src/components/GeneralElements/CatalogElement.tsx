import { CaretLeftOutlined, CaretRightOutlined } from "@ant-design/icons";
import { Flex } from "antd";
import CartManager from "../../CartManager";
import { useState, useEffect } from "react";

interface CatalogElementProps {
  productName: string;
  productCost: number;
  onChangeElementQuantity: (cost: string) => void; 
}

const CatalogElement = (props: CatalogElementProps) => {
  const [productAmount, setProductAmount] = useState(0);

  useEffect(() => {
    setProductAmount(CartManager.getProductQuantityFromCart(props.productName));
  }, [props.productName]);

  return (
    <div
      className="card text-bg-primary mb-3"
      style={{
        width: "200px",
        height: "220px",
        margin: "1%",
        textAlign: "center",
      }}
    >
      <div className="card-header" style={{ fontWeight: "700" }}>
        {props.productName}
      </div>
      <div className="card-body">
        <p className="card-text">
          Some quick example text to build on the card and make up the bulk of
          the card's content.
        </p>
        <p
          className="card-title"
          style={{ fontWeight: "700", textAlign: "center" }}
        >
          {props.productCost}$
        </p>
        <Flex
          style={{ width: "100%", userSelect: "none" }}
          justify="space-around"
          align="center"
        >
          <CaretLeftOutlined
            onClick={() => {
              CartManager.removeProduct(props.productName);
              if (!(productAmount === 0)) {
                setProductAmount(productAmount - 1);
                props.onChangeElementQuantity(localStorage.getItem("cartCost") || "0")
              }
            }}
          />
          {productAmount}
          <CaretRightOutlined
            onClick={() => {
              CartManager.addProduct(props.productName, props.productCost);
              setProductAmount(productAmount + 1);
              props.onChangeElementQuantity(localStorage.getItem("cartCost") || "0")
            }}
          />
        </Flex>
      </div>
    </div>
  );
};

export default CatalogElement;
