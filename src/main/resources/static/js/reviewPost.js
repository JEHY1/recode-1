document.addEventListener('DOMContentLoaded', function() {
    const selectAllCheckbox = document.getElementById('selectAll');
    const rowCheckboxes = document.querySelectorAll('.proList tbody input[type="checkbox"]');

    selectAllCheckbox.addEventListener('change', function() {
        rowCheckboxes.forEach(checkbox => {
            checkbox.checked = selectAllCheckbox.checked;
        });
    });

      // Numbering rows
      const rows = document.querySelectorAll('#proList tbody tr');
      rows.forEach((row, index) => {
          row.querySelector('td:nth-child(2)').textContent = index + 1;
      });
    });