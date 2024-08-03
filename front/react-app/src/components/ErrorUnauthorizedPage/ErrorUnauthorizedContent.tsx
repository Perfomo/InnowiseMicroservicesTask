import { Flex, Layout } from "antd";

const ErrorUnauthorizedContent: React.FC = () => {
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
            marginBottom: "10%",
          }}
        >
          <div
            className="card text-bg-danger mb-3"
            style={{ width: "300px", height: "170px", margin: "1%" }}
          >
            <div className="card-header">Oups... 401 error</div>
            <div className="card-body">
              <h5 className="card-title">
                You can't visit this page without authentication!
              </h5>
              <p className="card-text">Try to visit this page after login.</p>
            </div>
          </div>
        </Flex>
      </Layout>
    </>
  );
};

export default ErrorUnauthorizedContent;
