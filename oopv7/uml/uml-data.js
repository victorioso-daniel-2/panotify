// UML Data Structure for PaNotify Application
const umlData = {
    // Model Classes
    classes: [
        // User hierarchy
        {
            id: "user",
            name: "User",
            type: "class",
            category: "model",
            x: 200, y: 100,
            width: 200, height: 180,
            attributes: [
                "- userId: int",
                "- firstName: String",
                "- lastName: String",
                "- username: String",
                "- email: String",
                "- accountType: String"
            ],
            methods: [
                "+ getFullName(): String",
                "+ setPassword(String): void"
            ]
        },
        {
            id: "student",
            name: "Student",
            type: "class",
            category: "model",
            x: 100, y: 350,
            width: 200, height: 120,
            attributes: [
                "- enrolledCourses: List<CourseInfo>",
                "- examResults: List<ExamResult>"
            ],
            methods: [
                "+ addEnrolledCourse(CourseInfo): void",
                "+ getStudentId(): String"
            ]
        },
        {
            id: "instructor",
            name: "Instructor",
            type: "class",
            category: "model",
            x: 350, y: 350,
            width: 200, height: 120,
            attributes: [
                "- department: String",
                "- courses: List<Course>"
            ],
            methods: [
                "+ addCourse(Course): void",
                "+ getInstructorId(): String"
            ]
        },
        
        // Course and Exam classes
        {
            id: "course",
            name: "Course",
            type: "class",
            category: "model",
            x: 600, y: 100,
            width: 200, height: 180,
            attributes: [
                "- courseId: int",
                "- courseName: String",
                "- courseCode: String",
                "- instructorId: int",
                "- instructorName: String",
                "- exams: List<Exam>"
            ],
            methods: [
                "+ addExam(Exam): void",
                "+ addEnrollment(CourseEnrollment): void"
            ]
        },
        {
            id: "exam",
            name: "Exam",
            type: "class",
            category: "model",
            x: 600, y: 350,
            width: 200, height: 180,
            attributes: [
                "- examId: int",
                "- title: String",
                "- courseId: int",
                "- instructorId: int",
                "- deadline: LocalDateTime",
                "- questions: List<Question>"
            ],
            methods: [
                "+ isPublished(): boolean",
                "+ getDurationMinutes(): int"
            ]
        },
        {
            id: "question",
            name: "Question",
            type: "class",
            category: "model",
            x: 600, y: 600,
            width: 200, height: 140,
            attributes: [
                "- questionId: int",
                "- examId: int",
                "- questionText: String",
                "- options: String",
                "- correctOption: int"
            ],
            methods: [
                "+ getCorrectAnswer(): String"
            ]
        },
        
        // Answer and Result classes
        {
            id: "studentAnswer",
            name: "StudentAnswer",
            type: "class",
            category: "model",
            x: 350, y: 600,
            width: 200, height: 140,
            attributes: [
                "- studentId: int",
                "- questionId: int",
                "- answerText: String",
                "- isCorrect: boolean",
                "- submittedAt: LocalDateTime"
            ],
            methods: [
                "+ isCorrect(): boolean"
            ]
        },
        {
            id: "examResult",
            name: "ExamResult",
            type: "class",
            category: "model",
            x: 100, y: 600,
            width: 200, height: 140,
            attributes: [
                "- resultId: int",
                "- examId: int",
                "- studentId: int",
                "- totalScore: int",
                "- maxScore: int"
            ],
            methods: [
                "+ getScorePercentage(): double"
            ]
        },
        
        // Service Classes
        {
            id: "examService",
            name: "ExamService",
            type: "class",
            category: "service",
            x: 900, y: 350,
            width: 220, height: 180,
            attributes: [
                "- connection: Connection"
            ],
            methods: [
                "+ createExam(Exam): boolean",
                "+ addQuestion(Question): int",
                "+ getExamQuestions(int): List<Question>",
                "+ submitAnswer(StudentAnswer): boolean",
                "+ gradeExam(int, int): boolean",
                "+ getExamById(int): Exam",
                "+ getExamsForStudent(int): List<Exam>"
            ]
        },
        {
            id: "courseService",
            name: "CourseService",
            type: "class",
            category: "service",
            x: 900, y: 100,
            width: 220, height: 180,
            attributes: [
                "- connection: Connection"
            ],
            methods: [
                "+ createCourse(Course): boolean",
                "+ getCourseById(int): Course",
                "+ getInstructorCourses(int): List<Course>",
                "+ getEnrolledCourses(int): List<Course>",
                "+ enrollStudent(int, int): boolean",
                "+ getCourseStudents(int): List<Student>"
            ]
        },
        {
            id: "userService",
            name: "UserService",
            type: "class",
            category: "service",
            x: 900, y: 600,
            width: 220, height: 140,
            attributes: [
                "- connection: Connection"
            ],
            methods: [
                "+ createUser(User): boolean",
                "+ getUserById(int): User",
                "+ updateUser(User): boolean",
                "+ changePassword(int, String): boolean"
            ]
        },
        {
            id: "reportService",
            name: "ReportService",
            type: "class",
            category: "service",
            x: 900, y: 800,
            width: 220, height: 140,
            attributes: [
                "- connection: Connection"
            ],
            methods: [
                "+ getStudentReport(int, int): StudentReport",
                "+ getCourseReport(int): List<ExamResult>",
                "+ getClassPerformance(int): Map<String, Double>",
                "+ getTopPerformers(int): List<StudentReport>"
            ]
        },
        {
            id: "authService",
            name: "AuthenticationService",
            type: "class",
            category: "service",
            x: 600, y: 800,
            width: 220, height: 140,
            attributes: [
                "- connection: Connection"
            ],
            methods: [
                "+ login(String, String): User",
                "+ validateCredentials(String, String): boolean",
                "+ registerUser(User): boolean",
                "+ isUsernameAvailable(String): boolean"
            ]
        },
        
        // UI Classes
        {
            id: "loginView",
            name: "LoginView",
            type: "class",
            category: "ui",
            x: 450, y: 1000,
            width: 200, height: 100,
            attributes: [
                "- authService: AuthenticationService"
            ],
            methods: [
                "+ show(String): void"
            ]
        },
        {
            id: "studentDashboard",
            name: "StudentDashboard",
            type: "class",
            category: "ui",
            x: 200, y: 1000,
            width: 200, height: 120,
            attributes: [
                "- student: Student",
                "- tabs: Map<String, StudentTab>"
            ],
            methods: [
                "+ navigateToTab(String): void",
                "+ initializeTabs(): void"
            ]
        },
        {
            id: "instructorDashboard",
            name: "InstructorDashboard",
            type: "class",
            category: "ui",
            x: 700, y: 1000,
            width: 200, height: 120,
            attributes: [
                "- instructor: Instructor",
                "- tabs: Map<String, InstructorTab>"
            ],
            methods: [
                "+ navigateToTab(String): void",
                "+ initializeTabs(): void"
            ]
        },
        {
            id: "studentTab",
            name: "StudentTab",
            type: "interface",
            category: "ui",
            x: 150, y: 1150,
            width: 180, height: 80,
            methods: [
                "+ getContent(): VBox",
                "+ onNavigatedTo(): void"
            ]
        },
        {
            id: "instructorTab",
            name: "InstructorTab",
            type: "interface",
            category: "ui",
            x: 750, y: 1150,
            width: 180, height: 80,
            methods: [
                "+ getContent(): VBox",
                "+ onNavigatedTo(): void"
            ]
        }
    ],
    
    // Relationships between classes
    relationships: [
        // Inheritance relationships
        {
            id: "user_student",
            type: "inheritance",
            source: "student",
            target: "user",
            points: []
        },
        {
            id: "user_instructor",
            type: "inheritance",
            source: "instructor",
            target: "user",
            points: []
        },
        
        // Association relationships
        {
            id: "course_exam",
            type: "association",
            source: "course",
            target: "exam",
            sourceLabel: "1",
            targetLabel: "*",
            points: []
        },
        {
            id: "exam_question",
            type: "association",
            source: "exam",
            target: "question",
            sourceLabel: "1",
            targetLabel: "*",
            points: []
        },
        {
            id: "student_examResult",
            type: "association",
            source: "student",
            target: "examResult",
            sourceLabel: "1",
            targetLabel: "*",
            points: []
        },
        {
            id: "question_studentAnswer",
            type: "association",
            source: "question",
            target: "studentAnswer",
            sourceLabel: "1",
            targetLabel: "*",
            points: []
        },
        {
            id: "exam_examResult",
            type: "association",
            source: "exam",
            target: "examResult",
            sourceLabel: "1",
            targetLabel: "*",
            points: []
        },
        {
            id: "student_studentAnswer",
            type: "association",
            source: "student",
            target: "studentAnswer",
            sourceLabel: "1",
            targetLabel: "*",
            points: []
        },
        {
            id: "course_instructor",
            type: "association",
            source: "course",
            target: "instructor",
            sourceLabel: "*",
            targetLabel: "1",
            points: []
        },
        
        // Service to Model dependencies
        {
            id: "courseService_course",
            type: "dependency",
            source: "courseService",
            target: "course",
            points: []
        },
        {
            id: "examService_exam",
            type: "dependency",
            source: "examService",
            target: "exam",
            points: []
        },
        {
            id: "userService_user",
            type: "dependency",
            source: "userService",
            target: "user",
            points: []
        },
        {
            id: "authService_user",
            type: "dependency",
            source: "authService",
            target: "user",
            points: []
        },
        
        // UI to Service dependencies
        {
            id: "loginView_authService",
            type: "dependency",
            source: "loginView",
            target: "authService",
            points: []
        },
        {
            id: "studentDashboard_student",
            type: "dependency",
            source: "studentDashboard",
            target: "student",
            points: []
        },
        {
            id: "instructorDashboard_instructor",
            type: "dependency",
            source: "instructorDashboard",
            target: "instructor",
            points: []
        },
        
        // Implementation relationships
        {
            id: "studentTab_coursesTab",
            type: "implementation",
            source: "coursesTab",
            target: "studentTab",
            points: []
        },
        {
            id: "studentTab_examsTab",
            type: "implementation",
            source: "examsTab",
            target: "studentTab",
            points: []
        },
        {
            id: "instructorTab_managementTab",
            type: "implementation",
            source: "managementTab",
            target: "instructorTab",
            points: []
        }
    ]
}; 