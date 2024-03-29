import React, { useEffect, useState } from "react";
import SideBar from "../components/SideBar";
import Calendar from "react-calendar";
import "../components/utils/customCalendar.css";
import { useGlobalContext } from "../context/appContext";
import DashboardLeaveRequestTable from "../components/DashboardLeaveRequestTable";
import moment from "moment";
import Button from "@mui/material/Button";
import BasicModal from "../components/BasicModal";
import CounterCardWithIcon from "../components/CounterCardWithIcon";
import EventIcon from "@mui/icons-material/Event";
import EditCalendarIcon from "@mui/icons-material/EditCalendar";
import AssignmentIcon from "@mui/icons-material/Assignment";
import { Alert, AlertTitle, Slide } from "@mui/material";

function DashBoard() {
  const { authFetch, showAlert, alert, displayAlert } = useGlobalContext();
  const [datesWithNethovers, setDatesWithNethovers] = useState([]);
  const [currentEmployee, setCurrentEmployee] = useState([]);
  const [tasksAssignedToMe, setTasksAssignedToMe] = useState([]);
  const [pendingLeaves, setPendingLeaves] = useState([]);
  const [selectedDate, setSelectedDate] = useState({
    date: "0000-00-00",
    netHours: "00:00:00",
  });

  // const [open, setOpen] = useState(false);
  // const handleOpen = () => setOpen(true);
  // const handleClose = () => setOpen(false);

  useEffect(() => {
    authFetch
      .get("/punches/allDates")
      .then((res) => setDatesWithNethovers(res.data))
      .catch((err) => console.log(err));

    authFetch
      .get("/leave")
      .then((res) => {
        const tempPending = [];
        res.data.forEach((leaveRequest) => {
          if (leaveRequest.status.toString().toLowerCase() === "pending") {
            tempPending.push(leaveRequest);
          }
        });
        setPendingLeaves(tempPending);
      })
      .catch((err) => console.log(err));

    authFetch
      .get("/employee/getCurrent")
      .then((res) => setCurrentEmployee(res.data))
      .catch((err) => console.log(err));

    authFetch
      .get("/task/assignedToMe")
      .then((res) => setTasksAssignedToMe(res.data))
      .catch((err) => console.log(err));
  }, []);

  const setAbsent = (date) => {
    const formattedDate = moment(date).format("YYYY-MM-DD");
    const dateWithNetHours = datesWithNethovers.find(
      (x) => x.date === formattedDate
    );
    if (!dateWithNetHours && date < Date.now()) return "highlight";
  };

  const getNetHouersOfSelectedDate = (date) => {
    const formattedDate = moment(date).format("YYYY-MM-DD");
    const selecteDateWithNetHours = datesWithNethovers.find(
      (x) => x.date === formattedDate
    );
    if (selecteDateWithNetHours) setSelectedDate(selecteDateWithNetHours);
    else setSelectedDate({ date: formattedDate, netHours: "00:00:00" });
  };

  useEffect(() => {
    console.log(currentEmployee);
  }, [currentEmployee]);

  const cardData = [
    {
      count: pendingLeaves.length,
      heading: "Pending leave requests",
      icon: <EditCalendarIcon fontSize="large" />,
      color: "text-white",
      bg: "bg-red-500/70",
    },
    {
      count: currentEmployee.totalLeavesLeft,
      heading: "Total leaves left",
      icon: <EventIcon fontSize="large" />,
      color: "text-white",
      bg: "bg-green-600/80",
    },
    {
      count: tasksAssignedToMe?.filter(
        (task) => task.status.toLowerCase() !== "completed"
      ).length,
      heading: "Total task assigned",
      icon: <AssignmentIcon fontSize="large" />,
      color: "text-white",
      bg: "bg-blue-600/70",
    },
  ];

  return (
    <>
      <div className="absolute h-screen w-screen bg-black overflow-hidden">
        <div className="flex flex-row">
          <div className="left w-[15%]">
            <SideBar />
          </div>
          <div className="right w-[85%] h-screen">
            <div className="relative top-20 text-white flex justify-center text-xl">
              <h1>Dashboard</h1>
              {/* ------ Display alert start ------ */}
              {showAlert && (
                <div className="absolute right-10 z-50">
                  <Slide
                    direction="left"
                    in={"true"}
                    mountOnEnter
                    unmountOnExit
                  >
                    <Alert severity={alert.type}>
                      {/* <AlertTitle>Error</AlertTitle> */}
                      {alert.msg}
                    </Alert>
                  </Slide>
                </div>
              )}
              {/* ------ Display alert end ------ */}
            </div>
            <div className="flex">
              <div className="relative mx-[10px] top-24 w-[50%] flex flex-col justify-center bg-gray-300/40 backdrop-blur-md rounded-md p-[30px]">
                <h1 className="text-gray-200 mb-5 text-base">
                  Monthly attendance details
                </h1>
                <Calendar
                  showNeighboringMonth={false}
                  tileClassName={({ date }) => {
                    return setAbsent(date);
                  }}
                  onClickDay={(date) => getNetHouersOfSelectedDate(date)}
                />
                <div className="flex justify-start text-left mt-5">
                  <div className="w-full">
                    <h1 className="text-gray-200">Selected date:</h1>
                    &nbsp;&nbsp;
                    <div className="text-gray-400">
                      {selectedDate && <div>{selectedDate.date}</div>}
                    </div>
                  </div>
                  <br />
                  <div className="w-full">
                    <h1 className="text-gray-200">Net hours:</h1>
                    &nbsp;&nbsp;
                    <div className="text-gray-400">
                      {selectedDate && <div>{selectedDate.netHours}</div>}
                    </div>
                  </div>
                </div>
                {/* <Button onClick={handleOpen}>Open modal</Button> */}
              </div>
              <div className="relative mx-[10px] top-24 w-[50%] flex flex-col bg-gray-300/40 backdrop-blur-md rounded-md p-[30px]">
                <h1 className="text-gray-200 mb-5 text-base">Holidays list</h1>
                <Calendar />
              </div>
              <div className="relative mx-[10px] top-24 w-[50%] flex flex-col items-center bg-gray-300/40 backdrop-blur-md rounded-md p-[30px]">
                <div className="bg-gray-100/30 p-5 w-full h-full rounded-md flex flex-col justify-center">
                  {cardData.map((data) => (
                    <CounterCardWithIcon
                      count={data.count}
                      heading={data.heading}
                      icon={data.icon}
                      color={data.color}
                      bg={data.bg}
                      key={Math.random()}
                    />
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default DashBoard;
