import { Flex, Layout } from "antd";
import { useEffect, useState } from "react";
import CatalogElement from "../GeneralElements/CatalogElement";

const CatalogContent: React.FC = () => {
  const [catalogData, setCatalogData] = useState<any[]>([]);

  const fetchData = async () => {
    try {
      const response = await fetch(
        "http://172.17.0.1:8081/products/api/products",
        {
          method: "GET",
        }
      );

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      console.log("data", data);
      setCatalogData(data);
    } catch (error) {
      console.error("Error during login: ", error);
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
          {catalogData.map((item, index) => (
            <CatalogElement
              key={index}
              productName={item.name}
              productCost={item.cost}
            />
          ))}
        </Flex>
      </Layout>
    </>
  );
};

export default CatalogContent;
