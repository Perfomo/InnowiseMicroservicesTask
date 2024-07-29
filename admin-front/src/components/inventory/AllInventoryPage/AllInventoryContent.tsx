import { Flex, Layout } from "antd";
import { useEffect, useState } from "react";
import TokenManager from "../../general/generalElements/TokenManager";
import AllInventoryContentElement from "./AllInventoryContentElement";

const AllInventoryContent: React.FC = () => {
  const [users, setUsers] = useState<any[]>([]);

  const fetchData = async () => {
    try {
      const response = await fetch("/api/inventory/api/inventory", {
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
      setUsers(data);
    } catch (error) {
      console.error("Error during getting inventory: ", error);
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
          {users.map((item, index) => (
            <AllInventoryContentElement
              id={item.id}
              name={item.name}
              left={item.left}
              sold={item.sold}
              cost={item.cost}
              key={index}
            />
          ))}
        </Flex>
      </Layout>
    </>
  );
};

export default AllInventoryContent;
