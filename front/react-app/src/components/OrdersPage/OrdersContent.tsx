import { Flex, Layout } from "antd";
import { useEffect, useState } from "react";
import OrderContent from "../GeneralElements/OrderContetnt";
import ErrorNoOrders from "../GeneralElements/ErrorNoOrders";

const OrdersContent: React.FC = () => {
  const [orderData, setOrderData] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(true); 

  const fetchData = async () => {
    try {
      const response = await fetch(
        "/orders/api/"+ localStorage.getItem("username") +"/orders",
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
      setOrderData(data);
      setIsLoading(false);
    } catch (error) {
      console.error("Error during getting orders: ", error);
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  console.log(orderData)

  if (isLoading) {
    return <></>;
  }

  if (orderData.length === 0) {
    return (
      <ErrorNoOrders />
    );
  }

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
          {orderData.map((item, index) => (
            <OrderContent key={index} orderCost={item.cost} orderStatus={item.status} products={item.products} />
          ))}
        </Flex>
      </Layout>
    </>
  );
};

export default OrdersContent;
