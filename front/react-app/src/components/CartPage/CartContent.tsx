import React, { useState } from "react";
import { Flex, Layout } from "antd";
import CatalogElement from "../GeneralElements/CatalogElement";
import CartManager from "../../CartManager";
import ErrorEmptyCart from "../GeneralElements/ErrorEmptyCart";
import MakeOrderButton from "../GeneralElements/MakeOrderButton";

const CartContent: React.FC = () => {
  let cart = CartManager.getCartFromLocalStorage();
  const [totalCost, setTotalCost] = useState(localStorage.getItem("cartCost"));
  const handleElementQuantityChange = (newCost: string) => {
    setTotalCost(newCost);
  };

  return (
    <>
      <Layout style={{ height: "90vh", backgroundColor: "white" }}>
        <Flex
          justify="center"
          align="normal"
          wrap={true}
          style={{
            width: "94%",
            marginLeft: "3%",
            marginRight: "3%",
          }}
        >
          {cart.map(
            (
              item: { name: string; cost: number; quantity: number },
              index: React.Key | null | undefined
            ) => (
              <CatalogElement
                key={index}
                productName={item.name}
                productCost={item.cost}
                onChangeElementQuantity={handleElementQuantityChange}
              />
            )
          )}
        </Flex>
        <div style={{textAlign: "center"}}>
          {totalCost === "0" ? (
            <ErrorEmptyCart />
          ) : (
            <MakeOrderButton cost={"Make order - " + totalCost} />
          )}
        </div>
      </Layout>
    </>
  );
};

export default CartContent;
