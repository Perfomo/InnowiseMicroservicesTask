import { Flex, Layout } from "antd";

const ErrorEmptyCart: React.FC = () => {
  return (
    <>
      <Layout style={{ height: "90vh", backgroundColor: "white" }}>
        <Flex
          justify="center"
          align="center"
          wrap={true}
          style={{
            height: "100vh",
            width: "94%",
            marginLeft: "3%",
            marginRight: "3%",
            marginBottom: "10%"
          }}
        >
          <div
            className="card text-bg-danger mb-3"
            style={{ width: "300px", height: "150px", margin: "1%" }}
          >
            <div className="card-header">Oups... Your cart is empy</div>
            <div className="card-body">
              <h5 className="card-title">You can't create an order without produts.</h5>
              <p className="card-text">
                Add some products from catalog page.
              </p>
            </div>
          </div>
        </Flex>
      </Layout>
    </>
  );
};

export default ErrorEmptyCart;
