import { Flex, Layout } from "antd";
import { useEffect, useState } from "react";
import CatalogElement from "../GeneralElements/CatalogElement";
import OrderContent from "../GeneralElements/OrderContetnt";

const OrdersContent: React.FC = () => {
  const [orderData, setOrderData] = useState<any[]>([]);

  const fetchData = async () => {
    try {
      const response = await fetch(
        "http://172.17.0.1:8081/orders/api/"+ localStorage.getItem("username") +"/orders",
        {
          method: "GET",
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token"),
          },
        }
      );

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      console.log("data", data);
      setOrderData(data);
    } catch (error) {
      console.error("Error during getting orders: ", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <>
      <Layout style={{ height: "90vh", backgroundColor: "white" }}>
        <Flex
          justify="center"
          align="normal"
          wrap={true}
          style={{
            height: "100vh",
            width: "94%",
            marginLeft: "3%",
            marginRight: "3%",
          }}
        >
          {orderData.map((item, index) => (
            <OrderContent key={index} orderCost={item.cost} orderStatus={item.status} products={item.products} />
          ))}
        </Flex>
      </Layout>
    </>
  );
};

export default OrdersContent;
