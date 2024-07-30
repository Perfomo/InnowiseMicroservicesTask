import { Flex, Layout } from "antd";
import { useEffect, useState } from "react";
import TokenManager from "../../general/generalElements/TokenManager";
import AllOrdersContentElement from "./AllOrdersContentElement";

const AllOrdersContent: React.FC = () => {
  const [orders, setOrders] = useState<any[]>([]);

  const fetchData = async () => {
    try {
      const response = await fetch("/api/orders/api/orders", {
        method: "GET",
        headers: {
          "Authorization": "Bearer " + localStorage.getItem("token"),
        }
      });

      if (!response.ok) {
        if (response.status === 401) {
          try {
            TokenManager.tokenRefresh();
            window.location.reload();
          } catch (error) {
            console.log("error: " + error);
          }
        }
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      setOrders(data);
    } catch (error) {
      console.error("Error during getting orders: ", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <>
      <Layout style={{ backgroundColor: "white" }}>
        <Flex
          justify="center"
          align="normal"
          wrap={true}
          style={{
            width: "94%",
            marginTop: "1%",
            marginLeft: "3%",
            marginRight: "3%",
          }}
        >
          {orders.map((item, index) => (
            <AllOrdersContentElement
              id={item.id}
              username={item.username}
              products={item.products}
              orderStatus={item.status}
              orderCost={item.cost}
              key={index}
            />
          ))}
        </Flex>
      </Layout>
    </>
  );
};

export default AllOrdersContent;
