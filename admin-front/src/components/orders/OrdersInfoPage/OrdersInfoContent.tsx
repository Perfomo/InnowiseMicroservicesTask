import React, { CSSProperties, useEffect, useState } from "react";
import { Flex, Layout } from "antd";
import { useLocation, useNavigate } from "react-router-dom";
import TokenManager from "../../general/generalElements/TokenManager";
import BackButton from "../../general/generalElements/BackButton";
import DeleteButton from "../../general/generalElements/DeleteButton";

interface Product {
  [name: string]: number;
}

interface OrderData {
  id: number;
  status: string;
  username: string;
  products: Product;
  cost: number;
}

const OrdersInfoContent: React.FC = () => {
  const [orderData, setOrderData] = useState<OrderData | null>(null);
  const [isNotFound, setIsNotFound] = useState<boolean>(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { state } = location;
  const id = state?.id;
  const order = state?.order;

  const fetchData = async () => {
    try {
      const response = await fetch("/api/orders/api/orders/" + id, {
        method: "GET",
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      });

      if (!response.ok) {
        if (response.status === 401) {
          try {
            TokenManager.tokenRefresh();
            window.location.reload();
          } catch (error) {
            console.log("Error during token refresh: " + error);
          }
        }
        if (response.status === 404) {
          setIsNotFound(true);
          return;
        }
        throw new Error("Network response was not ok");
      }

      const data: OrderData = await response.json();
      setOrderData(data);
    } catch (error) {
      localStorage.clear();
      console.error("Error during getting info: ", error);
    }
  };

  useEffect(() => {
    if (!order) {
      fetchData();
    } else {
      setOrderData(order);
    }
  }, [order]);

  useEffect(() => {
    if (isNotFound) {
      navigate("/order/find");
    }
  }, [isNotFound, navigate]);

  const tdStyle: CSSProperties = {
    border: "1px solid blue",
    borderCollapse: "collapse",
    height: "45px",
  };

  const tdStyleButton: CSSProperties = {
    padding: "1%",
  };

  if (isNotFound) {
    return null;
  }

  return (
    <Layout style={{ height: "100vh", backgroundColor: "white" }}>
      <h1 style={{ textAlign: "center", marginBottom: "0%", marginTop: "1%" }}>
        Order info
      </h1>
      <Flex
        justify="center"
        align="normal"
        style={{ width: "100%", marginTop: "2%" }}
      >
        <table
          style={{
            width: "50%",
            height: "40%",
            textAlign: "center",
          }}
        >
          <tbody>
            <tr>
              <td style={tdStyle}>Order id</td>
              <td style={tdStyle}>{orderData?.id}</td>
            </tr>
            <tr>
              <td style={tdStyle}>Order status</td>
              <td style={tdStyle}>{orderData?.status}</td>
            </tr>
            <tr>
              <td style={tdStyle}>Username</td>
              <td style={tdStyle}>{orderData?.username}</td>
            </tr>
            <tr>
              <td style={tdStyle}>Products</td>
              <td style={tdStyle}>
                {orderData &&
                  orderData.products &&
                  Object.entries(orderData.products).map(
                    ([name, amount], index) => (
                      <p className="card-text" key={index}>
                        {name} - {String(amount)}
                      </p>
                    )
                  )}
              </td>
            </tr>
            <tr>
              <td style={tdStyle}>Cost</td>
              <td style={tdStyle}>{orderData?.cost}$</td>
            </tr>
          </tbody>
          <tfoot>
            <tr>
              <td style={tdStyleButton}>
                <BackButton />
              </td>
              <td style={{ width: "50%" }}>
                <DeleteButton searchEl={String(orderData?.id)} type="orders" />
              </td>
            </tr>
            {/* <tr>
              
              <td>
                <ChangeInfoButton
                  searchEl={String(inventoryData.id)}
                  type="inventory"
                />
              </td>
            </tr> */}
          </tfoot>
        </table>
      </Flex>
    </Layout>
  );
};

export default OrdersInfoContent;
