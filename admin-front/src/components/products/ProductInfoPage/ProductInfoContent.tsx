import React, { CSSProperties, useEffect, useState } from "react";
import { Flex, Layout } from "antd";
import { useLocation, useNavigate } from "react-router-dom";
import TokenManager from "../../general/generalElements/TokenManager";
import BackButton from "../../general/generalElements/BackButton";
import ProductInInventoryButton from "./ProductInInventoryButton";
import DeleteButton from "../../general/generalElements/DeleteButton";
import ChangeInfoButton from "../../general/generalElements/ChangeInfoButton";

const ProductInfoContent: React.FC = () => {
  const [productData, setProductData] = useState<any>([]);
  const [isNotFound, setIsNotFound] = useState<boolean>(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { state } = location;
  const value = state?.value;

  const fetchData = async () => {
    try {
      const response = await fetch("/api/products/api/products/name/" + value, {
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

      const data = await response.json();
      setProductData(data);
    } catch (error) {
      localStorage.clear();
      console.error("Error during getting info: ", error);
    }
  };

  useEffect(() => {
    if (!value.name) {
      fetchData();
    }
    else {
      setProductData(value);
    }
  }, []);

  useEffect(() => {
    if (isNotFound) {
      navigate("/products/find");
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
        Product info
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
              <td style={tdStyle}>Id</td>
              <td style={tdStyle}>{productData.id}</td>
            </tr>
            <tr>
              <td style={tdStyle}>Name</td>
              <td style={tdStyle}>{productData.name}</td>
            </tr>
            <tr>
              <td style={tdStyle}>Cost</td>
              <td style={tdStyle}>{productData.cost}$</td>
            </tr>
          </tbody>
          <tfoot>
            <tr>
              <td style={tdStyleButton}>
                <BackButton />
              </td>
              <td style={tdStyleButton}>
                <ProductInInventoryButton name={String(productData.name)} />
              </td>
            </tr>
            <tr>
              <td style={{ width: "50%" }}>
                <DeleteButton searchEl={String(productData.id)} type="products" />
              </td>
              <td>
                <ChangeInfoButton searchEl={String(productData.id)} type="products"/>
              </td>
            </tr>
          </tfoot>
        </table>
      </Flex>
    </Layout>
  );
};

export default ProductInfoContent;
