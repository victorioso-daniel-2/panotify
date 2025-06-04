document.addEventListener('DOMContentLoaded', function() {
    // Navigation highlighting
    const navLinks = document.querySelectorAll('.nav-link');
    const sections = document.querySelectorAll('.section');
    
    // Add theme toggle button to the navbar
    const navContainer = document.querySelector('.nav-container');
    if (navContainer) {
        const themeToggle = document.createElement('button');
        themeToggle.id = 'theme-toggle';
        themeToggle.className = 'nav-link';
        themeToggle.innerHTML = '<i class="fas fa-moon"></i> Dark Mode';
        themeToggle.style.marginLeft = 'auto';
        themeToggle.addEventListener('click', toggleDarkMode);
        navContainer.appendChild(themeToggle);
    }
    
    // Check if any link was clicked and activate it
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            navLinks.forEach(link => link.classList.remove('active'));
            this.classList.add('active');
        });
    });
    
    // Highlight active section on scroll
    window.addEventListener('scroll', function() {
        let current = '';
        
        sections.forEach(section => {
            const sectionTop = section.offsetTop;
            const sectionHeight = section.clientHeight;
            if (pageYOffset >= (sectionTop - 200)) {
                current = section.getAttribute('id');
            }
        });
        
        navLinks.forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('href') === `#${current}`) {
                link.classList.add('active');
            }
        });
    });
    
    // UML diagram controls
    setupDiagramControls();
    
    // Check for saved theme preference
    const savedTheme = localStorage.getItem('darkMode');
    if (savedTheme === 'enabled') {
        document.body.classList.add('dark-mode-enabled');
        const themeToggle = document.getElementById('theme-toggle');
        if (themeToggle) {
            themeToggle.innerHTML = '<i class="fas fa-sun"></i> Light Mode';
        }
    }
});

function setupDiagramControls() {
    // Zoom controls
    const zoomIn = document.getElementById('zoom-in');
    const zoomOut = document.getElementById('zoom-out');
    const resetZoom = document.getElementById('reset-zoom');
    
    if (zoomIn && zoomOut && resetZoom) {
        let scale = 1;
        const diagram = document.querySelector('.interactive-svg');
        
        zoomIn.addEventListener('click', function() {
            scale += 0.1;
            if (diagram) {
                diagram.style.transform = `scale(${scale})`;
            }
        });
        
        zoomOut.addEventListener('click', function() {
            scale -= 0.1;
            if (scale < 0.5) scale = 0.5;
            if (diagram) {
                diagram.style.transform = `scale(${scale})`;
            }
        });
        
        resetZoom.addEventListener('click', function() {
            scale = 1;
            if (diagram) {
                diagram.style.transform = `scale(${scale})`;
            }
        });
    }
    
    // Class type filters
    setupClassFilters();
    
    // Relationship filters
    setupRelationshipFilters();
}

function setupClassFilters() {
    const modelCheckbox = document.getElementById('show-model');
    const serviceCheckbox = document.getElementById('show-service');
    const uiCheckbox = document.getElementById('show-ui');
    const utilCheckbox = document.getElementById('show-util');
    
    if (modelCheckbox && serviceCheckbox && uiCheckbox && utilCheckbox) {
        const modelClasses = document.querySelectorAll('.model-class');
        const serviceClasses = document.querySelectorAll('.service-class');
        const uiClasses = document.querySelectorAll('.ui-class');
        const utilClasses = document.querySelectorAll('.util-class');
        
        modelCheckbox.addEventListener('change', function() {
            modelClasses.forEach(el => {
                el.style.display = this.checked ? 'block' : 'none';
            });
        });
        
        serviceCheckbox.addEventListener('change', function() {
            serviceClasses.forEach(el => {
                el.style.display = this.checked ? 'block' : 'none';
            });
        });
        
        uiCheckbox.addEventListener('change', function() {
            uiClasses.forEach(el => {
                el.style.display = this.checked ? 'block' : 'none';
            });
        });
        
        utilCheckbox.addEventListener('change', function() {
            utilClasses.forEach(el => {
                el.style.display = this.checked ? 'block' : 'none';
            });
        });
    }
}

function setupRelationshipFilters() {
    const inheritanceCheckbox = document.getElementById('show-inheritance');
    const associationCheckbox = document.getElementById('show-association');
    const dependencyCheckbox = document.getElementById('show-dependency');
    
    if (inheritanceCheckbox && associationCheckbox && dependencyCheckbox) {
        const inheritanceLines = document.querySelectorAll('.inheritance-relationship');
        const associationLines = document.querySelectorAll('.association-relationship');
        const dependencyLines = document.querySelectorAll('.dependency-relationship');
        
        inheritanceCheckbox.addEventListener('change', function() {
            inheritanceLines.forEach(el => {
                el.style.display = this.checked ? 'block' : 'none';
            });
        });
        
        associationCheckbox.addEventListener('change', function() {
            associationLines.forEach(el => {
                el.style.display = this.checked ? 'block' : 'none';
            });
        });
        
        dependencyCheckbox.addEventListener('change', function() {
            dependencyLines.forEach(el => {
                el.style.display = this.checked ? 'block' : 'none';
            });
        });
    }
}

function toggleDarkMode() {
    document.body.classList.toggle('dark-mode-enabled');
    
    const themeToggle = document.getElementById('theme-toggle');
    if (themeToggle) {
        if (document.body.classList.contains('dark-mode-enabled')) {
            themeToggle.innerHTML = '<i class="fas fa-sun"></i> Light Mode';
        } else {
            themeToggle.innerHTML = '<i class="fas fa-moon"></i> Dark Mode';
        }
    }
    
    // Save preference in localStorage
    if (document.body.classList.contains('dark-mode-enabled')) {
        localStorage.setItem('darkMode', 'enabled');
    } else {
        localStorage.setItem('darkMode', 'disabled');
    }
} 