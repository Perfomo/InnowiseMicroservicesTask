import { Flex, Layout } from "antd";
import AllOrdersContentElement from "../AllOrdersPage/AllOrdersContentElement";

interface FindUserOrdersContentProps {
    orders: any[];
}

const FindUserOrdersContent: React.FC<FindUserOrdersContentProps> = ({orders}) => {

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

export default FindUserOrdersContent;
