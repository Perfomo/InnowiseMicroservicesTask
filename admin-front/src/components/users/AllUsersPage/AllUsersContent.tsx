import { Flex, Layout } from "antd";
import { useEffect, useState } from "react";
import AllUSersContentElement from "./AllUsersContentElement";
import TokenManager from "../../general/generalElements/TokenManager";

const AllUsersContent: React.FC = () => {
  const [users, setUsers] = useState<any[]>([]);

  const fetchData = async () => {
    try {
      const response = await fetch("/api/users/api/users", {
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
      console.error("Error during getting users: ", error);
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
            <AllUSersContentElement
              username={item.username}
              userId={item.id}
              email={item.email}
              roles={["men", "women"]}
              key={index}
            />
          ))}
        </Flex>
      </Layout>
    </>
  );
};

export default AllUsersContent;
