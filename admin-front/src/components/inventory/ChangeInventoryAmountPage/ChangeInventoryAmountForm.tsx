import React from "react";
import { Flex, Form, Layout } from "antd";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";
import InventoryAmountForm from "./InventoryAmountForm";

const ChangeInventoryAmountForm: React.FC = () => {
  const [form] = Form.useForm();
  const navigate = useNavigate();

  const location = useLocation();
  const { state } = location;
  const value = state?.value;

  const onFinish = (values: any) => {
    interface ErrorField {
      name: string;
      errors: string[];
    }
    axios
      .put(
        `/api/inventory/api/inventory/${value}/amount?amount=${values.amount}`, null,
        {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token"),
          },
        }
      )
      .then(() => {
        form.resetFields();
        navigate("/inventory/menu");
      })
      .catch((error) => {
        console.log(error)
        if (error.response?.data) {
          const errorData = error.response.data;

          const errorFields: ErrorField[] = Object.entries(errorData).map(
            ([field, error]) => ({
              name: field,
              errors: Array.isArray(error) ? error : [error],
            })
          );
          console.log(errorFields);

          form.setFields(errorFields);
        }
      });
  };

  return (
    <Layout style={{ height: "90vh", backgroundColor: "white" }}>
      <Flex
        justify="center"
        align="center"
        style={{ height: "100vh", width: "100%" }}
      >
        <InventoryAmountForm
          form={form}
          onFinish={onFinish}
          buttonText="Change amount"
        />
      </Flex>
    </Layout>
  );
};

export default ChangeInventoryAmountForm;
