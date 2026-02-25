import pandas as pd
import matplotlib.pyplot as plt
import sys

def generate_throughput_plot(csv_file, output_file='throughput_plot.png', thread_count=None):
    print(f"Reading data from {csv_file}...")
    df = pd.read_csv(csv_file)

    print(f"Total records: {len(df)}")

    min_time = df['start_time'].min()
    df['second'] = (df['start_time'] - min_time) // 1000
    throughput = df.groupby('second').size()

    plt.figure(figsize=(12, 6))
    plt.plot(throughput.index, throughput.values, linewidth=1, color='black')

    plt.xlabel('Time (seconds)', fontsize=12)
    plt.ylabel('Throughput (req/s)', fontsize=12)

    # Dynamic title for Assignment 3
    if thread_count is None:
        plt.title('CS6650 Assignment 3: Checkout Throughput Over Time', fontsize=14)
    else:
        plt.title(
            f'CS6650 Assignment 3: Checkout Throughput Over Time\n({thread_count} threads)',
            fontsize=14
        )

    plt.grid(True, alpha=0.3)

    avg_throughput = throughput.mean()
    max_throughput = throughput.max()
    min_throughput = throughput.min()

    stats_text = (
        f'Avg: {avg_throughput:.0f} req/s\n'
        f'Max: {max_throughput:.0f} req/s\n'
        f'Min: {min_throughput:.0f} req/s'
    )
    plt.annotate(
        stats_text,
        xy=(0.02, 0.98),
        xycoords='axes fraction',
        verticalalignment='top',
        fontsize=10,
        bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.5)
    )

    plt.tight_layout()
    plt.savefig(output_file, dpi=150)
    print(f"Plot saved to {output_file}")

    print("THROUGHPUT STATISTICS")
    print("=" * 50)
    print(f"Average throughput: {avg_throughput:.2f} req/s")
    print(f"Maximum throughput: {max_throughput} req/s")
    print(f"Minimum throughput: {min_throughput} req/s")
    print(f"Test duration: {throughput.index.max()} seconds")
    print("=" * 50)

    # Keep headless-safe behavior
    # plt.show()

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("Usage: python plot.py <csv_file> [output_png] [thread_count]")
        sys.exit(1)

    csv_file = sys.argv[1]
    output_file = sys.argv[2] if len(sys.argv) > 2 else 'throughput_plot.png'
    thread_count = int(sys.argv[3]) if len(sys.argv) > 3 else None

    generate_throughput_plot(csv_file, output_file, thread_count)
