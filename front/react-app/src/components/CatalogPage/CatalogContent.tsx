import { Flex, Layout } from "antd";
import { useEffect, useState } from "react";
import CatalogElement from "../GeneralElements/CatalogElement";
import CostTag from "../GeneralElements/CostTag";

const CatalogContent: React.FC = () => {
  const [catalogData, setCatalogData] = useState<any[]>([]);
  const [totalCost, setTotalCost] = useState(localStorage.getItem("cartCost"));
  const handleElementQuantityChange = (newCost: string) => {
    setTotalCost(newCost);
  };
  const fetchData = async () => {
    try {
      const response = await fetch(
        "/products/api/products",
        {
          method: "GET",
        }
      );

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      setCatalogData(data);
    } catch (error) {
      console.error("Error during getting products: ", error);
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
            marginLeft: "3%",
            marginRight: "3%",
          }}
        >
          {catalogData.map((item, index) => (
            <CatalogElement
              key={index}
              productName={item.name}
              productCost={item.cost}
              onChangeElementQuantity={handleElementQuantityChange}
            />
          ))}
        </Flex>
      </Layout>
      <footer
        style={{
          position: "fixed",
          bottom: "0",
          width: "100%",
          textAlign: "center",
        }}
      >
        <CostTag cost={totalCost} />
      </footer>
    </>
  );
};

export default CatalogContent;
